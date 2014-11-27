package com.getgigradio.gigradio.tests;

import com.getgigradio.gigradio.RobolectricGradleTestRunner;
import com.getgigradio.gigradio.activity.MainActivity;

import org.boundbox.BoundBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.util.ActivityController;

@RunWith(RobolectricGradleTestRunner.class)
public class MyClassTest {

//    private BoundBoxOfMainActivity boundBoxOfMainActivity;

    @Test
    @BoundBox(boundClass = MainActivity.class)
    public void testWhenActivityCreatedHelloTextViewIsVisible() throws Exception {
        MainActivity activity = new MainActivity();

//        boundBoxOfMainActivity = new BoundBoxOfMainActivity(activity);

        ActivityController.of(activity).attach().create();



//        int visibility = activity.findViewById(R.id.).getVisibility();
//        assertEquals(visibility, View.VISIBLE);
    }
}
