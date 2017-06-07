/**
 * MIT License
 *
 * Copyright (c) 2017 Demitrius Belai
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package br.unesp.fc.countdown;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Countdown {

    private final ScheduledExecutorService scheduler =
       Executors.newScheduledThreadPool(1);

    private int time;

    private long countTime;
    private long startTime;
    private Runnable action = null;
    private Runnable startEvent = null;
    private Runnable stopEvent = null;

    private ScheduledFuture scheduledFuture;

    public void setTime(int time) {
        this.time = time;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setStartEvent(Runnable startEvent) {
        this.startEvent = startEvent;
    }

    public void setStopEvent(Runnable stopEvent) {
        this.stopEvent = stopEvent;
    }

    public void start() {
        if (!isStarted()) {
            startTime = System.currentTimeMillis();
            scheduledFuture = scheduler.scheduleAtFixedRate(() -> tick(), 0, 100, TimeUnit.MILLISECONDS);
            if (startEvent != null)
                startEvent.run();
        }
    }

    public void stop() {
        if (isStarted()) {
            countTime -= System.currentTimeMillis() - startTime;
            scheduledFuture.cancel(false);
            scheduledFuture = null;
            if (stopEvent != null)
                stopEvent.run();
        }
    }

    public void reset() {
        if (!isStarted()) {
            countTime = (time + 1) * 1000 - 1;
            tick();
        }
    }

    private volatile int currentTime = 0;

    public int getCurrentTime() {
        return currentTime;
    }

    private void tick() {
        int time;
        if (scheduledFuture != null)
            time = (int)Math.floorDiv(countTime - System.currentTimeMillis() + startTime, 1000);
        else
            time = (int)countTime / 1000;
        if (currentTime != time) {
            currentTime = time;
            if (action != null)
                action.run();
        }
    }

    public boolean isStarted() {
        return scheduledFuture != null;
    }

    private void destroy() {
        if (isStarted())
            scheduledFuture.cancel(false);
        scheduler.shutdown();
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
    }

}
