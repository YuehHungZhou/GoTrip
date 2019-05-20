package com.topdsr2.gotrip.trip;

import com.topdsr2.gotrip.data.GoTripRepository;
import com.topdsr2.gotrip.data.object.Point;
import com.topdsr2.gotrip.data.object.TripAndPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TripPresenterTest {
    private ArrayList<Point> points;
    private ArrayList<Point> pointsDayHolder;

    @Mock
    GoTripRepository mGoTripRepository;
    @Mock
    TripContract.View mTripView;
    @Mock
    TripPresenter mTripPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTripPresenter = new TripPresenter(mGoTripRepository, mTripView);

        points = new ArrayList<Point>();

        Point point1 = new Point();
        point1.setSorte(1);
        points.add(point1);

        Point point2 = new Point();
        point2.setSorte(2);
        points.add(point2);

        Point point4 = new Point();
        point4.setSorte(4);
        points.add(point4);

        Point point3 = new Point();
        point3.setSorte(3);
        points.add(point3);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        pointsDayHolder = mTripPresenter.sortPoint(points);
        assertEquals(points.size(), pointsDayHolder.get(points.size() - 1).getSorte());
    }

}