package autotest.entity;

import autotest.dto.custData.ClientDataItem;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class TicketData {

    public TicketData(Consumer<TicketData> builder){
        builder.accept(this);
    }


    private String price;   //Сумма оплаты
    private String ownerFIO;    //Фамилия имя пассажира
    private String dateDepartureForward;    //Дата вылета туда
    private String dateDepartureBackward;   //Дата вылета обратно

    private String ticketId;         // Id билета
    private String bookingId;       //Id брони
    private String ticketStatus;    //Состояние брони/оплаты
    private String datePayment;      //Дата оплаты

    private ClientDataItem clientDataItem; //Данные по клиенту

    @Override
    public String toString() {
        return "TicketData{" +
                "price='" + price + '\'' +
                ", ownerFIO='" + ownerFIO + '\'' +
                ", dateDepartureForward='" + dateDepartureForward + '\'' +
                ", dateDepartureBackward='" + dateDepartureBackward + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                ", datePayment='" + datePayment + '\'' +
                ", clientDataItem=" + clientDataItem.toString() +
                '}';
    }
}
