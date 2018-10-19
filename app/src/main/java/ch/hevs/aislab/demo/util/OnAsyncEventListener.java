package ch.hevs.aislab.demo.util;

/**
 * This generic interface is used as custom callback for async tasks.
 * We can delete It, was for the register
 */
public interface OnAsyncEventListener {
    void onSuccess();
    void onFailure(Exception e);
}
