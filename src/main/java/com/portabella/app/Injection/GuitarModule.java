package com.portabella.app.Injection;

/**
 * Created by omerrom on 07/06/2018.
 */

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GuitarModule {

    @Provides
    @Singleton
    public EventBus provideEventBus() {

        return EventBus.getDefault();

    }
}
