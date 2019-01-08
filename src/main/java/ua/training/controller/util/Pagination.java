package ua.training.controller.util;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {
    private static final int ELEM_ON_PAGE = 5;

    public List<T> getPageList(List<T> entityList, int page) {
        List<T> listForPage = new ArrayList<>();

        int beginIdx = (page - 1) * ELEM_ON_PAGE;
        int endIdx = beginIdx + ELEM_ON_PAGE;

        if (entityList.size() >= endIdx + 1) {
            return entityList.subList(beginIdx, endIdx);
        }

        if (entityList.size() >= beginIdx) {
            for (int i = beginIdx; i < entityList.size(); i++) {
                listForPage.add(entityList.get(i));
            }
        }

        return listForPage;
    }

    public int getPageNumber(List<T> entityList) {
        return (int) Math.ceil((double) entityList.size() / ELEM_ON_PAGE);
    }
}