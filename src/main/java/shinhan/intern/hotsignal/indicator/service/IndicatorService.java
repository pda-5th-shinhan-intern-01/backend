package shinhan.intern.hotsignal.indicator.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public List<IndicatorEventResponse> getIndicatorEvents() {
        return eventRepository.findAllWithIndicator().stream()
            .map((EconomicEvent event) -> IndicatorEventResponse.builder()
                .name(event.getName())
                .indicator(IndicatorEventResponse.IndicatorDto.builder()
                    .name(event.getIndicator().getName())
                    .code(event.getIndicator().getCode())
                    .build())
                .date(event.getDate().format(formatter))
                .expectedValue(event.getForecast())
                .prevValue(event.getPrevious())
                .actualValue(event.getActual())
                .unit("%") 
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
                .date(event.getDate().format(formatter))
                .expectedValue(event.getForecast())
                .prevValue(event.getPrevious())
                .actualValue(event.getActual())
                .unit("%")
                .build())
            .collect(Collectors.toList());
    }

    public List<ChartDataResponse> getIndicatorChartData(String indicatorCode) {
        return indicatorRepository.findByCode(indicatorCode).stream()
            .map(indicator -> new ChartDataResponse(
                indicator.getDate().toString(),
                indicator.getValue()
            ))
            .collect(Collectors.toList());
    }
}

