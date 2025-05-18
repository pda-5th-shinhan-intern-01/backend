package shinhan.intern.hotsignal.indicator.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.dto.ChartDataResponse;
import shinhan.intern.hotsignal.indicator.dto.IndicatorEventResponse;
import shinhan.intern.hotsignal.indicator.entity.EconomicEvent;
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
}

