package ua.training.controller.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PaginationTest {
    private Pagination<String> pagination = new Pagination<>();
    private List<String> strings = new ArrayList<>();
    private int elemOnPage = 5;

    @Before
    public void setUp() {
        strings.add("elem1");
        strings.add("elem2");
        strings.add("elem3");
        strings.add("elem4");
        strings.add("elem5");
        strings.add("elem6");
        strings.add("elem7");
        strings.add("elem8");
        strings.add("elem9");
        strings.add("elem10");
        strings.add("elem11");
        strings.add("elem12");
        strings.add("elem13");
        strings.add("elem14");
        strings.add("elem15");
        strings.add("elem16");
    }

    @Test
    public void getPageList() {
        int page = 2;

        pagination.getPageList(strings, page);

        assertEquals(strings.subList(elemOnPage * page - elemOnPage, elemOnPage * page),
                pagination.getPageList(strings, page));
    }

    @Test
    public void getPageNumber() {
        int expected = (int) Math.ceil((double) strings.size() / elemOnPage);
        assertEquals(expected, pagination.getPageNumber(strings));
    }
}