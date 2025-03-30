package dev.lotnest.dernbot.core.utils;

public abstract class StoppableThread extends Thread {
    private boolean isActive = true;

    protected StoppableThread() {
    }

    public void terminate() {
        isActive = false;
        interrupt();
        cleanUp();
    }

    protected abstract void cleanUp();
}
