package autotest.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class TicketData {

    public TicketData(Consumer<TicketData> build){
        build.accept(this);
    }


    private String price;   //Сумма оплаты
    private String ownerFIO;    //Фамилия имя пассажира
    private String dateDepartureForward;    //Дата вылета туда
    private String dateDepartureBackward;   //Дата вылета обратно

    private String ticketId;         // Id билета
    private String bookingId;       //Id брони
    private String ticketStatus;    //Состояние брони/оплаты
    private String datePayment;      //Дата оплаты

}
