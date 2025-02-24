package org.norsh.api.service;

import org.norsh.api.config.ApiConfig;
import org.norsh.exceptions.OperationStatus;
import org.norsh.model.transport.DataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingRequestService {
    private final CacheService cacheService;
    private final QueueProducerService queueService;
    private final ApiConfig apiConfig = ApiConfig.getInstance();

    @Autowired
    public MessagingRequestService(CacheService cacheService, QueueProducerService queueService) {
        this.cacheService = cacheService;
        this.queueService = queueService;
    }

    /**
     * Sends a message to the queue and saves its status in the cache.
     *
     * @param request The request ID.
     * @param data    The request data to send.
     * @return A RequestStatusDto indicating the initial status (PROCESSING).
     */
    public DataTransfer request(DataTransfer transport) {
    	transport.setStatus(OperationStatus.CREATED);
        queueService.send(transport.getRequestId(), transport);
        cacheService.save(transport.getRequestId(), transport, apiConfig.getDefaultsConfig().getMessagingTtlMs());

        return transport;
    }

    /**
     * Sends a message to the queue and waits for its status.
     *
     * @param request The request ID.
     * @param data    The request data to send.
     * @return A RequestStatusDto indicating the final status or TIMEOUT if no response.
     */
    public DataTransfer requestAndWait(DataTransfer transport) {
    	transport.setStatus(OperationStatus.CREATED);
    	cacheService.delete(transport.getRequestId());

    	queueService.send(transport.getRequestId(), transport);
        DataTransfer transportResponse = cacheService.get(transport.getRequestId(), DataTransfer.class, apiConfig.getDefaultsConfig().getMessagingTimeoutMs());

        if (transportResponse == null) {
            transportResponse = new DataTransfer(transport.getRequestId(), OperationStatus.TIMEOUT);
        }

        return transportResponse;
    }
    
//    public TransportDtos get(String request) {
//    	TransportDtos requestStatus = cacheService.get(request, TransportDtos.class);
//    	if (requestStatus != null)
//    		return requestStatus;
//    	
//    	
//    	return sendAndWait(request, new TransportDtos(request, TransportStatus.PROCESSING));
//    }
}
