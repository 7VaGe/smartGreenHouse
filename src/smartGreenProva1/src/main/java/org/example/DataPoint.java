package org.example;

class DataPoint {
    private double value;
    private long time;
    private String place;

    public DataPoint(double value, long time, String place) {
        this.value = value;
        this.time = time;
        this.place = place;
    }
    /**
     * Getter for return the value of a DataPoint value entry
     *
     * @return DataPoint value
     * */
    public double getValue() {
        return value;
    }
    /**
     * Getter for return the time of a DataPoint time entry, indicates the current time millis of an DataPoint object
     *
     * @return DataPoint time
     * */
    public long getTime() {
        return time;
    }
    /**
     * Getter for return the place of a DataPoint place entry, indicates where this DataPoint object is coming from
     *
     * @return DataPoint place
     * */
    public String getPlace() {
        return place;
    }
}
