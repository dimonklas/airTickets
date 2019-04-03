package autotest.entity;

import lombok.Getter;

import java.util.HashSet;

@Getter
public class AirportsData {

    public HashSet<String> getLondonAirports() {
        HashSet<String> arrayList = new HashSet<>();
        arrayList.add("Гатвик (Лондон)");
        arrayList.add("Хитроу (Лондон)");
//        arrayList.add("Станстед (Лондон)");
        arrayList.add("Лондон Сити (Лондон)");
        return arrayList;
    }

    public HashSet<String> getNewYorkAirports() {
        HashSet<String> arrayList = new HashSet<>();
        arrayList.add("Джон Ф. Кеннеди (Нью-Йорк)");
        arrayList.add("Ньюарк Либерти Интернешнл (Нью-Йорк)");
        arrayList.add("Ла-Гардия (Нью-Йорк)");
        return arrayList;
    }
}
