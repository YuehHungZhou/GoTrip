package com.topdsr2.gotrip.home;

import com.topdsr2.gotrip.data.GoTripLocalDataSource;
import com.topdsr2.gotrip.data.GoTripRemoteDataSource;
import com.topdsr2.gotrip.data.GoTripRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class HomePresenterTest {


    @Mock
    GoTripRepository mGoTripRepository;
    HomeFragment mHomeFragment;
    HomePresenter mHomePresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mHomeFragment = new HomeFragment();
        mHomePresenter = new HomePresenter(mGoTripRepository, mHomeFragment);
        mHomeFragment.setPresenter(mHomePresenter);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {

    }
}