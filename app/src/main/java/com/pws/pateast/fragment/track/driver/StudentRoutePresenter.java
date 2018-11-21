package com.pws.pateast.fragment.track.driver;

import android.app.Activity;
import android.content.Intent;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.DriverService;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.locationmanager.LocationPresenter;
import com.pws.pateast.enums.BoardStatus;
import com.pws.pateast.enums.TripRecordType;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.SocketEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CONFIRM_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_RECORD_STATUS_CHANGED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_CONNECTED;

/**
 * Created by intel on 18-Sep-17.
 */

public class StudentRoutePresenter extends LocationPresenter<StudentRouteView> {

    DriverService driverService;

    @Override
    public void attachView(StudentRouteView view) {
        super.attachView(view);
        eventBus.post(new SocketEvent(EVENT_IS_CONNECTED));
        getView().onActionClick();
    }

    @Override
    public void onSocketEvent() {

    }

    @Override
    public void onLocationEvent(LocationEvent event) {
        TripEvent tripEvent = event.getTripEvent();
        switch (event.getEventType()) {
            case EVENT_TRIP_RECORD_STATUS_CHANGED:
                onTripEvent(tripEvent);
                break;
        }
    }

    public void onTripEvent(TripEvent tripEvent) {
        switch (tripEvent.getTripEvent()) {
            case EVENT_START_PICKUP_ON_BOARD:
                changeTripRecordStatus(tripEvent.getTripRecordIds(), BoardStatus.PARENT_ON_BOARD_DROP);
                break;
            default:
                if (tripEvent.isStatus())
                    getView().updateStudentRouteAdapter(tripEvent.getTripRecordId(), tripEvent.getTripRecordStatus());
                else
                    showMessage(R.string.unable_to_connect);
                break;
        }
    }

    public void getTrip() {
        driverService = serviceBuilder.createService(DriverService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(getView().getTrip().getId()));

        disposable = driverService.getTrip(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<Trip>>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<Trip> response) {
                        if (response.getData() != null) {
                            getView().setTrip(response.getData());
                            List<Trip> tripList = getStudentRouteTripRecord(getView().getTripRecords());
                            if (!tripList.isEmpty()) {
                                getView().setStudentRouteAdapter(tripList);
                            } else {
                                onError(new RetrofitException(getString(R.string.no_trip)));
                            }
                        } else {
                            onError(new RetrofitException(getString(R.string.no_trip)));
                        }
                    }

                    @Override
                    public StudentRoutePresenter getPresenter() {
                        return StudentRoutePresenter.this;
                    }
                });
    }

    public void changeTripRecordStatus(List<Integer> tripRecordIds, BoardStatus boardStatus) {
        List<Integer> positions = new ArrayList<>();
        int position = 0;
        for (Trip tripRecord : getView().getTripRecords()) {
            for (int tripRecordId : tripRecordIds) {
                if (tripRecord.getId() == tripRecordId) {
                    tripRecord.setStatus(boardStatus.getStatus());
                    positions.add(position);
                    break;
                }
            }
            position++;
        }
        for (int iPosition : positions) {
            Collections.swap(getView().getTripRecords(), 0, iPosition);
        }
        getView().updateStudentRouteAdapter(-1, 0);
    }

    public void changeTripRecordStatus(int tripRecordId, int tripRecordStatus, boolean isCancel) {
        if (!connected) {
            showMessage(R.string.unable_to_connect);
            return;
        }
        getView().hideDialog();
        TripStatus tripStatus = TripStatus.getStatus(getView().getTrip().getStatus());
        String eventType = null;
        BoardStatus boardStatus = BoardStatus.getStatus(tripRecordStatus);
        switch (boardStatus) {
            case PARENT_ON_BOARD_DROP:
                boardStatus = isCancel ? BoardStatus.NONE : BoardStatus.DRIVER_ON_BOARD_PICKUP;
                eventType = isCancel ? EVENT_CANCEL_PICKUP_ON_BOARD : EVENT_CONFIRM_PICKUP_ON_BOARD;
                break;
            case DRIVER_ON_BOARD_PICKUP:
                if (tripStatus == TripStatus.PICKUP_STARTED) {
                    boardStatus = BoardStatus.DRIVER_OFF_BOARD_PICKUP;
                    eventType = EVENT_STOP_PICKUP_OFF_BOARD;
                    break;
                }
                showMessage(R.string.message_driver_cannot_off_board_pickUp);
                return;
            case DRIVER_OFF_BOARD_PICKUP:
            case NONE:
                if (tripStatus == TripStatus.PICKUP_STOPPED) {
                    boardStatus = isCancel ? BoardStatus.PARENT_PICKUP_EARLY : BoardStatus.DRIVER_ON_BOARD_DROP;
                    eventType = isCancel ? EVENT_CANCEL_DROP_ON_BOARD : EVENT_START_DROP_ON_BOARD;
                    break;
                }
                showMessage(R.string.message_driver_cannot_on_board);
                return;
            case DRIVER_ON_BOARD_DROP:
                if (tripStatus == TripStatus.DROP_STARTED) {
                    boardStatus = BoardStatus.DRIVER_OFF_BOARD_DROP;
                    eventType = EVENT_STOP_DROP_OFF_BOARD;
                    break;
                }
                showMessage(R.string.message_driver_cannot_off_board_drop);
                return;
        }
        if (eventType != null) {
            eventBus.post(new LocationEvent(eventType,
                    new TripEvent.Builder()
                            .setUser_type(UserType.DRIVER.getValue())
                            .setTripRecordId(tripRecordId)
                            .setTripRecordStatus(boardStatus.getStatus())
                            .build()));
        }
    }


    private List<Trip> getStudentRouteTripRecord(List<Trip> tripList) {
        return StreamSupport
                .stream(tripList)
                .filter(trip -> getTripRecord(trip))
                .collect(Collectors.toList());
    }

    private boolean getTripRecord(Trip tripRecord) {
        if (tripRecord.getType() == TripRecordType.BOTH.getStatus()) {
            return true;
        }
        Trip trip = getView().getTrip();
        switch (TripStatus.getStatus(trip.getStatus())) {
            case NOT_STARTED:
            case PICKUP_STARTED:
                return tripRecord.getType() == TripRecordType.PICKUP_ONLY.getStatus();
            case PICKUP_STOPPED:
            case DROP_STARTED:
                return tripRecord.getType() == TripRecordType.DROP_OFF_ONLY.getStatus();
            case DROP_STOPPED:
            default:
                return false;
        }
    }

    @Override
    public void detachView() {
        Activity activity = (Activity) getContext();
        Intent intent = new Intent();
        intent.putExtra(Extras.TRIP, getView().getTrip());
        activity.setResult(Activity.RESULT_OK, intent);
        super.detachView();
    }
}
