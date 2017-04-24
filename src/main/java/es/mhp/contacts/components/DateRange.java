package es.mhp.contacts.components;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

/**
 * Created by MHP on 20/04/2017.
 */
public class DateRange  {
    private LocalDate beginDate;

    private LocalDate endDate;

    public static DateRange between(LocalDate beginDate, LocalDate endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setBeginDate(beginDate);
        dateRange.setEndDate(endDate);
        return dateRange;
    }



    public LocalDate getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Period getPeriod() {
        LocalDate beginDate = Optional.ofNullable(this.beginDate)
                .orElse(LocalDate.MIN);
        LocalDate endDate = Optional.ofNullable(this.endDate)
                .orElse(LocalDate.MAX);
        return Period.between(beginDate, endDate);
    }

//    @Override
//    public long get(TemporalUnit unit) {
//        return this.getPeriod()
//                .get(unit);
//    }
//
//    @Override
//    public List<TemporalUnit> getUnits() {
//        return this.getPeriod()
//                .getUnits();
//    }
//
//    @Override
//    public Temporal addTo(Temporal temporal) {
//        return this.getPeriod()
//                .addTo(temporal);
//    }
//
//    @Override
//    public Temporal subtractFrom(Temporal temporal) {
//        return this.getPeriod()
//                .subtractFrom(temporal);
//    }

    @Override
    public String toString() {
        return this.getPeriod()
                .toString();
    }
}
