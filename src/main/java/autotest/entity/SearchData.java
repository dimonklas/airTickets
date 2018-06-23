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

    private String channel;                  //Канал обслуживания
    private String waysType;                 //'Только туда', 'Туда и обратно', 'Сложный маршрут'
    private boolean plusMinus3days = false;  //Чекбокс '+/- 3 дня'
    private String classType;                //'Эконом', 'Бизнес'
    private String departureCity;            //Город вылета
    private String arrivalCity;              //Город прилета
    private String departureCity_2;          //Город вылета 2
    private String arrivalCity_2;            //Город прилета 2
    private String departureCity_3;          //Город вылета 3
    private String arrivalCity_3;            //Город прилета 3
    private String departureCity_4;          //Город вылета 4
    private String arrivalCity_4;            //Город прилета 4
    private String departureDateForward;     //Дата вылета туда
    private String departureDateBackward;    //Дата вылета обратно
    private int daysFwd;                     // Вылет туде через ... дней
    private int daysBckwd;                   // Вылет обратно через ... дней

    private int passengersCount;  //Количество пассажиров общее
    private int adultsCount = 1;    //Кол-во взрослых
    private int childCount = 0;      //Кол-во детей от 2-12 лет
    private int infantCount = 0;    //кол-во младенцев < 2лет

    private boolean fakeDoc = false;    //При заполнении клиентских данных нет при себе паспорта
    private boolean fakeDocChld = false;    //При заполнении клиентских данных нет при себе паспорта ребенка
    private boolean fakeDocInf = false;    //При заполнении клиентских данных нет при себе паспорта младенца
}
