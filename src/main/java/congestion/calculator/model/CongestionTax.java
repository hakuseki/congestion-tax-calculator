package congestion.calculator.model;

// tag::CongestionTax[]
// tag::CongestionTax[]
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * The class CongestionTax
 *
 * @author  maw, 2022-05-30
 * @version 1.0
 */
public class CongestionTax {
    String regNo;
    Vehicle vehicle;
    LocalDateTime localDateTime;

    public String getRegNo() {
        return regNo;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public static final class CongestionTaxBuilder {
        private String regNo;
        private Vehicle vehicle;
        private LocalDateTime localDateTime;

        public CongestionTaxBuilder(final String regNo) {
            this.regNo = regNo;
        }

        public CongestionTaxBuilder() {
        }

        public static CongestionTaxBuilder aCongestionTax() {
            return new CongestionTaxBuilder();
        }

        public CongestionTaxBuilder withRegNo(final String regNo) {
            this.regNo = regNo;
            return this;
        }

        public CongestionTaxBuilder withVehicle(final Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public CongestionTaxBuilder withLocalDateTime(final LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public CongestionTax build() {
            final CongestionTax congestionTax = new CongestionTax();
            congestionTax.localDateTime = this.localDateTime;
            congestionTax.regNo = this.regNo;
            congestionTax.vehicle = this.vehicle;
            return congestionTax;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CongestionTax.class.getSimpleName() + "[", "]")
                .add("regNo='" + regNo + "'")
                .add("vehicle=" + vehicle)
                .add("localDateTime=" + localDateTime)
                .toString();
    }
}
//end::CongestionTax[]
