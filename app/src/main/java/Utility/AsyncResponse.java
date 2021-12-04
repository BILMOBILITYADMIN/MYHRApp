package Utility;

/**
 * Created by Deeksha on 15-12-2015.
 * Interface class to capture the result of an Asynch Task
 */
public interface AsyncResponse {
    void processFinish(String output, int status_code, String url, int type);

    void configurationUpdated(boolean configUpdated);
}
