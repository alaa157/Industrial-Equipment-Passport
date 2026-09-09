package com.industrial.maintenance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industrial.maintenance.entity.MaintenanceRequest;
import com.industrial.maintenance.repository.MaintenanceRequestRepository;
import com.industrial.maintenance.service.*;
import org.junit.jupiter.api.Test;

class InspectionFailureConsumerTest {
@Test
void failedInspectionCreatesMaintenanceRequest(){
MaintenanceRequestRepository repository=mock(MaintenanceRequestRepository.class);
MaintenanceEventPublisher publisher=mock(MaintenanceEventPublisher.class);
ObjectMapper mapper=new ObjectMapper();

when(repository.save(any(MaintenanceRequest.class))).thenAnswer(inv->inv.getArgument(0));

InspectionFailureConsumer consumer=new InspectionFailureConsumer(repository,publisher,mapper);

String event="""
{
"type":"InspectionFailedEvent",
"payload":{
"equipmentId":"11111111-1111-1111-1111-111111111111",
"inspectorId":"22222222-2222-2222-2222-222222222222"
}
}
""";

assertDoesNotThrow(()->consumer.consume(event));
verify(repository).save(any(MaintenanceRequest.class));
verify(publisher).publish(eq("MaintenanceRequestCreatedEvent"),any(MaintenanceRequest.class));
}
}
