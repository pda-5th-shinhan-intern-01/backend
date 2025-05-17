package shinhan.intern.hotsignal.indicator.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.dto.IndicatorEventResponse;
import shinhan.intern.hotsignal.indicator.entity.EconomicEvent;
import shinhan.intern.hotsignal.indicator.repository.EconomicEventRepository;

@Service
@RequiredArgsConstructor
public class IndicatorService {
    private final EconomicEventRepository eventRepository;
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
}

