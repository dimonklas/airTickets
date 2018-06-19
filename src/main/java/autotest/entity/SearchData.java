package autotest.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
public class SearchData {

    public SearchData(Consumer<SearchData> build){
        build.accept(this);
    }


    private String waysType;                 //'Только туда', 'Туда и обратно', 'Сложный маршрут'
    private boolean plusMinus3days = false;  //Чекбокс '+/- 3 дня'
    private String classType;                //'Эконом', 'Бизнес'
    private String departureCity;            //Город вылета
    private String arrivalCity;              //Город прилета
    private String departureCity_2;          //Город вылета 2
    private String arrivalCity_2;            //Город прилета 2
    private String departureDateForward;     //Дата вылета туда
    private String departureDateBackward;    //Дата вылета обратно

    private int passengersCount;  //Количество пассажиров общее
    private int adultsCount = 1;    //Кол-во взрослых
    private int kidsCount = 0;      //Кол-во детей от 2-12 лет
    private int babiesCount = 0;    //кол-во младенцев < 2лет


}
