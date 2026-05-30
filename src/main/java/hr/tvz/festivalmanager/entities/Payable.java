package hr.tvz.festivalmanager.entities;

import java.math.BigDecimal;

public sealed interface Payable permits Artist, Worker {
    BigDecimal getFee();
}
