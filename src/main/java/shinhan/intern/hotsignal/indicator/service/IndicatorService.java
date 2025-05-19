package shinhan.intern.hotsignal.indicator.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.dto.ChartDataResponse;
import shinhan.intern.hotsignal.indicator.dto.IndicatorDTO;
import shinhan.intern.hotsignal.indicator.dto.IndicatorEventResponse;
import shinhan.intern.hotsignal.indicator.dto.IndicatorInfoDTO;
import shinhan.intern.hotsignal.indicator.entity.EconomicEvent;
import shinhan.intern.hotsignal.indicator.entity.Indicator;
import shinhan.intern.hotsignal.indicator.repository.EconomicEventRepository;
import shinhan.intern.hotsignal.indicator.repository.IndicatorRepository;

@Service
@RequiredArgsConstructor
public class IndicatorService {
    private final EconomicEventRepository eventRepository;
    private final IndicatorRepository indicatorRepository;
    private final EconomicEventRepository economicEventRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public List<IndicatorEventResponse> getIndicatorEvents() {
        return eventRepository.findAllWithIndicator().stream()
            .map((EconomicEvent event) -> IndicatorEventResponse.builder()
                .name(event.getName())
                .indicator(IndicatorEventResponse.IndicatorDto.builder()
                    .name(event.getIndicator().getName())
                    .code(event.getIndicator().getCode())
                    .build())
                .date(event.getDate().format(formatter)).time(event.getTime().toString())
                .expectedValue(event.getForecast())
                .prevValue(event.getPrevious())
                .actualValue(event.getActual())
                .unit(extractUnit(event.getPrevious()) != null ? extractUnit(event.getPrevious()) : "%").country("USA")
                .build())
            .collect(Collectors.toList());
    }

    public List<IndicatorEventResponse> getIndicatorEventsByIndicatorId(Long id) {
        return eventRepository.findAllWithIndicator().stream()
            .filter(event -> event.getIndicator() != null && event.getIndicator().getId().equals(id))
            .map(event -> IndicatorEventResponse.builder()
                .name(event.getName())
                .indicator(IndicatorEventResponse.IndicatorDto.builder()
                    .name(event.getIndicator().getName())
                    .code(event.getIndicator().getCode())
                    .build())
                .date(event.getDate().format(formatter)).time(event.getTime().toString())
                .expectedValue(
                        Optional.ofNullable(parseDouble(event.getForecast()))
                                    .map(Object::toString)
                                    .orElse(""))
                .prevValue(
                        Optional.ofNullable(parseDouble(event.getPrevious()))
                                    .map(Object::toString)
                                    .orElse(""))
                .actualValue(
                        Optional.ofNullable(parseDouble(event.getActual()))
                                .map(Object::toString)
                                .orElse("")
                )
                .unit(extractUnit(event.getPrevious())  != null ? extractUnit(event.getPrevious()) : "%").country("USA")
                .build())
            .collect(Collectors.toList());
    }

    public List<ChartDataResponse> getIndicatorChartData(String indicatorCode) {
        return indicatorRepository.findAllByCode(indicatorCode).stream()
            .map(indicator -> new ChartDataResponse(
                indicator.getDate().toString(),
                indicator.getValue()
            ))
            .collect(Collectors.toList());
    }

    public List<IndicatorEventResponse> getAllEconomicEvents() {
        return economicEventRepository.findAll().stream()
                .map(event -> IndicatorEventResponse.builder()
                        .name(event.getName())
                        .indicator(event.getIndicator() != null ?
                                IndicatorEventResponse.IndicatorDto.builder()
                                        .name(event.getIndicator().getName())
                                        .code(event.getIndicator().getCode())
                                        .build()
                                :null)
                        .country("USA")
                        .actualValue(
                                Optional.ofNullable(parseDouble(event.getActual()))
                                        .map(Object::toString)
                                        .orElse("")
                        )
                        .prevValue(
                                Optional.ofNullable(parseDouble(event.getPrevious()))
                                        .map(Object::toString)
                                        .orElse(""))
                        .expectedValue(
                                Optional.ofNullable(parseDouble(event.getForecast()))
                                        .map(Object::toString)
                                        .orElse(""))
                        .unit(
                                Optional.ofNullable(extractUnit(event.getPrevious()))
                                        .orElse("%")
                        )
                        .date(event.getDate().format(formatter))
                        .time(String.valueOf(event.getTime()))
                        .build())
                .collect(Collectors.toList());
    }


    //파싱 메소드
    private Double parseDouble(String value) {
        if (value == null) return null;
        try {
            // 숫자와 소수점만 남기고 파싱
            String numeric = value.replaceAll("[^0-9.\\-]", "");
            return numeric.isEmpty() ? null : Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractUnit(String value) {
        if (value == null) return null;
        // 숫자와 . 만 제거 → 남는 건 단위
        return value.replaceAll("[0-9.\\-]", "").trim();
    }

    public IndicatorInfoDTO getIndicatorLatest(String indicatorCode) {
        Indicator indicator = indicatorRepository.findTopByCodeOrderByDateDesc(indicatorCode);
        // 해당 지표 이름에 대해서 예상치 있는지 찾아보고
        Optional<EconomicEvent> Oevent = economicEventRepository.findFirstByIndicatorAndDateAfterOrderByDateAsc(indicator, LocalDate.now());

        if(Oevent.isEmpty()){
            // 없으면
            return IndicatorInfoDTO.builder()
                    .indicatorName(indicator.getName())
                    .indicatorCode(indicatorCode)
                    .next(0.0)
                    .delta(0.0)
                    .prev(indicator.getValue())
                    .unit(resolveUnit(indicator.getCode(), ""))
                    .build();
        }
        EconomicEvent event = Oevent.get();
        return IndicatorInfoDTO.builder()
                .indicatorCode(indicatorCode)
                .indicatorName(indicator.getName())
                .next(Optional.ofNullable(parseDouble(event.getForecast()))
                        .orElse(0.0))
                .prev(Optional.ofNullable(parseDouble(event.getPrevious()))
                        .orElse(0.0))
                .delta(Optional.ofNullable(parseDouble(event.getForecast()))
                        .flatMap(forecast ->
                                Optional.ofNullable(parseDouble(event.getPrevious()))
                                        .map(prev -> forecast - prev)
                        ).orElse(0.0))
                .unit(extractUnit(event.getPrevious()) != null ? extractUnit(event.getPrevious()) : "%")
                .build();

    }

    private String resolveUnit(String code, String rawUnit) {
        if (rawUnit != null && !rawUnit.isBlank()) return rawUnit;

        return switch (code.toUpperCase()) {
            case "CPI", "PPI", "CORE_PCE", "GDP", "UNEMPLOYMENT",
                 "CORE_CPI", "CORE_PPI", "INDUSTRIAL_PRODUCTION" -> "%";
            case "ISM" -> "";
            case "NFP","RETAIL_SALES" -> "K";
            default -> "unknown";
        };
    }

    public List<IndicatorDTO> getAllIndicators() {
        return indicatorRepository.findAll().stream()
                .sorted(Comparator.comparing(Indicator::getDate).reversed())  // 최신순
                .map(i -> IndicatorDTO.builder()
                        .name(i.getName())
                        .code(i.getCode())
                        .date(i.getDate())
                        .value(i.getValue())
                        .unit(resolveUnit(i.getCode(),""))  // null 방지
                        .build())
                .collect(Collectors.toList());

    }
}

