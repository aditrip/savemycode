package oracle.kv.impl.tif.esclient.esResponse;

import oracle.kv.impl.tif.esclient.restClient.RestResponse;


/**
 * A TimeOrderedResponse is queued up in a PriorityQueue.
 * The natural ordering used for priority queue 
 * is defined on the basis of most recent response.
 * 
 * This is required because monitoring client sends
 * request to get available ES Nodes periodically.
 * It is possible that one request may get stuck for long,
 * because the ES Node the request went to is not responding.
 * A later request may go to different node and get a response.
 * 
 * Latest Response is one with the latest cluster state.
 * 
 */

public class TimeOrderedResponse implements Comparable<TimeOrderedResponse>{
    
    private RestResponse response;
    private long timeInNanos;
    
    public TimeOrderedResponse(RestResponse response, long timeInNanos) {
        this.response = response;
        this.timeInNanos = timeInNanos;
    }
   
    
    @Override
    public int compareTo(TimeOrderedResponse o) {
       return compareTo(this,o);
    }
    
    
    private int compareTo(TimeOrderedResponse o1, TimeOrderedResponse o2) {
           if(o1.getTimeInNanos() > o2.getTimeInNanos()) {
               return 1;
           } else if (o1.getTimeInNanos() < o2.getTimeInNanos()) {
               return -1;
           } else {
               return 0;
           }
    }

    public RestResponse getResponse() {
        return response;
    }

    public void setResponse(RestResponse getNodesResponse) {
        this.response = getNodesResponse;
    }
    
    
    public long getTimeInNanos() {
        return timeInNanos;
    }

    public void setTimeInNanos(long timeInNanos) {
        this.timeInNanos = timeInNanos;
    }


 

}
