package io.github.wickeddroidmx.plugin.utils.time;

public class TimeFormatter {
    private final Format format;
    private final float timeFloat;
    public TimeFormatter(String time) {
        char c = time.charAt(time.length() - 1);
        String t = time.substring(0, time.length()-1);

        switch (c) {
            case 't' -> this.format = Format.TICKS;
            case 's' -> this.format = Format.SECONDS;
            case 'm' -> this.format = Format.MINUTES;
            case 'h' -> this.format = Format.HOURS;
            default -> throw new RuntimeException("Invalid time format");
        }

        try {
            this.timeFloat = Long.parseLong(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TimeFormatter(float timeFloat, Format format) {
        this.timeFloat = timeFloat;
        this.format = format;
    }

    public float convertTo(Format f) {
        float n = timeFloat;

        if(f.priority == format.priority) {
            return timeFloat;
        }

        if(f.priority > format.priority) {
            for(int i = format.priority; i < f.priority; i++) {
                n = n / this.getByPriority(i).getCalc();
            }

            return n;
        }

        for(int i = format.priority-1; i > f.priority-1; i--) {
            n = n * this.getByPriority(i).getCalc();
        }

        return n;
    }

    public float getTimeFloat() {
        return timeFloat;
    }

    public Format getFormat() {
        return format;
    }

    public enum Format {
        TICKS(1, 20),
        SECONDS(2, 60),
        MINUTES(3, 60),
        HOURS(4, 60);

        private int priority;
        private int calc;
        Format(int priority, int calc) {
            this.priority = priority;
            this.calc = calc;
        }

        public int getPriority() {
            return priority;
        }

        public int getCalc() {
            return calc;
        }
    }
    public Format getByPriority(int n) {
        switch (n) {
            case 1: return Format.TICKS;
            case 2: return Format.SECONDS;
            case 3: return Format.MINUTES;
            case 4: return Format.HOURS;
            default: throw new RuntimeException("Unknown Format with that priority");
        }
    }
}
