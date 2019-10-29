import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVTester {
    public static void main(String[] args) throws IOException {
        String fileName = "src/main/resources/Lancaster_County_School_Salaries.csv";
        numberOfDistricts(getTeacherList(fileName));

    }

    public static List<Teacher> getTeacherList(String fileName) throws IOException {
        List<Teacher> teacherList = new ArrayList<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
        ) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Teacher.class);//jakiej klasy obiekty w zawartosc pliku
            String[] memberFieldsToBindTo = {"name", "surname", "salary", "district", "position"};//nazwa pól klasy wedlug kolejnosci w pliku csv
            strategy.setColumnMapping(memberFieldsToBindTo);
            //zamiana csv na Teacher
            //skip line 1 (naglowek)
            //ignore leading white spaces
            CsvToBean<Teacher> csvToBean = new CsvToBeanBuilder(reader).withMappingStrategy(strategy).withSkipLines(1).withIgnoreLeadingWhiteSpace(true).build();
            Iterator<Teacher> teacherIterator = csvToBean.iterator();
            while (teacherIterator.hasNext()) {
                teacherList.add(teacherIterator.next());
            }
        }
        return teacherList;
    }

    public static int numberOfTeachersSalaryGrater(int salary, List<Teacher> teacherList) {
        int numberOfTeachers = 0;
        for (Teacher teacher : teacherList) {
            if (teacher.getSalary() > salary) {
                numberOfTeachers++;
            }
        }
        return numberOfTeachers;
    }

    public static int numberOfTeachersSalaryLess(int salary, List<Teacher> teacherList) {
        int numberOfTeachers = 0;
        for (Teacher teacher : teacherList) {
            if (teacher.getSalary() < salary) {
                numberOfTeachers++;
            }
        }
        return numberOfTeachers;
    }

    public static double[] meanStD(List<Teacher> teacherList) {
        double mean = 0;
        double std = 0;
        int i = 0;
        for (Teacher teacher : teacherList) {
            if (teacher.getSalary() != 0) {
                mean += mean;
                i++;
            }
        }
        mean /= i;
        for (Teacher teacher : teacherList) {
            if (teacher.getSalary() != 0)
                std += Math.pow(teacher.getSalary() - mean, 2);
        }
        std = Math.sqrt(std / (i - 1));
        return new double[]{mean, std};
    }

    public static int numberOfDistricts(List<Teacher> teacherList) {
        List<String> districtList = teacherList.stream().map(t -> t.getDistrict()).collect(Collectors.toList());
        Map<String ,List<Teacher>> result=teacherList.stream().collect(Collectors.groupingBy(Teacher::getDistrict));
        return result.size();
    }
}