package ru.itchannel.ycsearcher.dto;

import java.util.Objects;

public class PerformanceSummary {
    private double currentSpeed;
    private double maxSpeed;
    private double minSpeed;
    private int channelsCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceSummary that = (PerformanceSummary) o;
        return Objects.equals(getCurrentSpeed(), that.getCurrentSpeed()) &&
                Objects.equals(getMaxSpeed(), that.getMaxSpeed()) &&
                Objects.equals(getChannelsCount(), that.getChannelsCount()) &&
                Objects.equals(getMinSpeed(), that.getMinSpeed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentSpeed(), getMaxSpeed(), getMinSpeed(), getChannelsCount());
    }

    @Override
    public String toString() {
        return "PerformanceSummary{" +
                "currentSpeed='" + currentSpeed + '\'' +
                ", maxSpeed='" + maxSpeed + '\'' +
                ", minSpeed='" + minSpeed + '\'' +
                ", minSpeed='" + channelsCount + '\'' +
                '}';
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setChannelsCount(int channelsCount) {
        this.channelsCount = channelsCount;
    }

    public int getChannelsCount() {
        return channelsCount;
    }
}
