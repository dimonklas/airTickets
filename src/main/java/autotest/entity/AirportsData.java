package autotest.entity;

import lombok.Getter;

import java.util.HashSet;

@Getter
public class AirportsData {

    public HashSet<String> getLondonAirports() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("Гатвик (Лондон)");
        hashSet.add("Хитроу (Лондон)");
//        arrayList.add("Станстед (Лондон)");
        hashSet.add("Лондон Сити (Лондон)");
        return hashSet;
    }

    public HashSet<String> getNewYorkAirports() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("Джон Ф. Кеннеди (Нью-Йорк)");
        hashSet.add("Ньюарк Либерти Интернешнл (Нью-Йорк)");
        hashSet.add("Ла-Гардия (Нью-Йорк)");
        return hashSet;
    }
}
