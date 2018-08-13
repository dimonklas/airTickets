package autotest.dataproviders;


import autotest.entity.forDataproviders.DateTests;

public class Dataproviders {

    public static Object[][] dataForDepartureAndArrivalFields() {
        return new String[][] {
                {
                    ""
                },
                {
                        "北京位於華北平原的"
                },
                {
                        "<script>alert()</script>"
                },
                {
                        "qwerйцк"
                },
                {
                        "<!@#\"'%^*(_+=/?.,"
                },
                {
                        "12345"
                }
        };
    }


    public static DateTests[][] dataForBirthdayField() {
        return new DateTests[][] {
                {DateTests.builder().dateValue("").errorText("Заполните поле").build()},
                {DateTests.builder().dateValue("qwerйцк").errorText("Заполните поле").build()},
                {DateTests.builder().dateValue("<!@#\"'%^*(_+=/?.,").errorText("Заполните поле").build()},
                {DateTests.builder().dateValue("01.01.1850").errorText("Возраст взрослого должен быть меньше 120 лет").build()},
                {DateTests.builder().dateValue("01.01.2014").errorText("На момент последнего прилета возраст взрослого должен быть больше 12 лет").build()},
                {DateTests.builder().dateValue("01.01.2021").errorText("Дата рождения должна быть меньше даты последнего прилета").build()},
                {DateTests.builder().dateValue("01.43.5345").errorText("Некорректная дата").build()},
                {DateTests.builder().dateValue("12.13.1984").errorText("Некорректная дата").build()},
                {DateTests.builder().dateValue("29.02.1985").errorText("Некорректная дата").build()},
                {DateTests.builder().dateValue("29.02.1984").errorText(null).build()}
        };
    }


}