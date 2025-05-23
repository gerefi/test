package com.gerefi.ui.widgets;

import com.gerefi.core.Sensor;
import com.gerefi.core.SensorCategory;
import com.gerefi.core.SensorCentral;
import com.gerefi.core.ui.AutoupdateUtil;
import com.gerefi.ui.GaugesPanel;
import com.gerefi.ui.UIContext;
import com.gerefi.ui.util.UiUtils;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Round gauge
 *
 * On double-click a {@link DetachedSensor} is created
 *
 * Date: 7/9/14
 * Andrey Belomutskiy, (c) 2013-2020
 * @see GaugesPanel
 */

public class SensorGauge {
    private static final String HINT_LINE_1 = "Double-click to detach";
    private static final String HINT_LINE_2 = "Right-click to change";

    public static Component createGauge(UIContext uiContext, Sensor sensor, GaugeChangeListener listener, JMenuItem extraMenuItem) {
        JPanelWithListener wrapper = new JPanelWithListener(new BorderLayout());

        createGaugeBody(uiContext, sensor, wrapper, listener, extraMenuItem);

        return wrapper;
    }

    public interface GaugeChangeListener {
        /**
         * This event happens when user decides to switch the kind of gauge
         * displayed by this control
         *
         * @param sensor new type
         */
        void onSensorChange(Sensor sensor);
    }

    public static void createGaugeBody(UIContext uiContext, final Sensor sensor, final JPanelWithListener wrapper, final GaugeChangeListener listener,
                                       final JMenuItem extraMenuItem) {
        final Radial gauge = createRadial(sensor.getName(), sensor.getUnits(), sensor.getMaxValue(), sensor.getMinValue());

        UiUtils.setToolTip(gauge, HINT_LINE_1, HINT_LINE_2);
        UiUtils.setToolTip(wrapper, HINT_LINE_1, HINT_LINE_2);

        gauge.setBackgroundColor(BackgroundColor.LIGHT_GRAY);

        SensorCentral.getInstance().addListener(sensor,
            value -> {
                if (GaugesPanel.IS_PAUSED)
                    return;
                gauge.setValue(sensor.translateValue(value));
            }
        );

        gauge.setValue(sensor.translateValue(SensorCentral.getInstance().getValue(sensor)));
        gauge.setLcdDecimals(2);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(uiContext, e, wrapper, listener, extraMenuItem);
                } else if (e.getClickCount() == 2) {
                    handleDoubleClick(uiContext, e, gauge, sensor);
                }
            }
        };
        gauge.addMouseListener(mouseListener);
        wrapper.removeAllChildrenAndListeners();
        wrapper.addMouseListener(mouseListener);
        wrapper.add(gauge, BorderLayout.CENTER);
        AutoupdateUtil.trueLayout(wrapper.getParent());
    }

    private static void showPopupMenu(UIContext uiContext, MouseEvent e, JPanelWithListener wrapper, GaugeChangeListener listener,
                                      JMenuItem extraMenuItem) {
        JPopupMenu pm = new JPopupMenu();
        fillGaugeMenuItems(uiContext, pm, wrapper, listener, extraMenuItem);
        if (extraMenuItem != null)
            pm.add(extraMenuItem);
        pm.show(e.getComponent(), e.getX(), e.getY());
    }

    private static void fillGaugeMenuItems(UIContext uiContext, JPopupMenu popupMenu, final JPanelWithListener wrapper, final GaugeChangeListener listener, final JMenuItem extraMenuItem) {
        for (final SensorCategory sc : SensorCategory.values()) {
            JMenuItem cmi = new JMenu(sc.getName());
            popupMenu.add(cmi);

            for (final Sensor s : Sensor.getSensorsForCategory(sc.getName())) {
                JMenuItem mi = new JMenuItem(s.getName());
                mi.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createGaugeBody(uiContext, s, wrapper, listener, extraMenuItem);
                        listener.onSensorChange(s);
                    }
                });
                cmi.add(mi);
            }
        }
    }

    private static void handleDoubleClick(UIContext uiContext, MouseEvent e, Radial gauge, Sensor sensor) {
        int width = gauge.getSize().width;
        final DetachedSensor ds = new DetachedSensor(uiContext, sensor, width);

        ds.show(e);
    }

    public static Radial createRadial(String title, String units, double maxValue, double minValue) {
        Radial radial1 = new Radial();
        radial1.setTitle(title);
        radial1.setUnitString(units);

        radial1.setMinValue(minValue);
        if (minValue == maxValue) {
            // a bit of a hack to survive not great input data
            radial1.setMaxValue(minValue + 10);
        } else {
            radial1.setMaxValue(maxValue);
        }
        radial1.setThresholdVisible(false);
        radial1.setPointerColor(ColorDef.RED);

        radial1.setValue(0);
        return radial1;
    }
}


