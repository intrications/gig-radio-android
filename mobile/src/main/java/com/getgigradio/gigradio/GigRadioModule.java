package com.getgigradio.gigradio;

import dagger.Module;

@Module(
    includes = {
        DataModule.class
    },
    injects = {
        GigRadioApp.class
    }
)
public final class GigRadioModule {
  private final GigRadioApp app;

  public GigRadioModule(GigRadioApp app) {
    this.app = app;
  }

//  @Provides @Singleton Application provideApplication() {
//    return app;
//  }
}