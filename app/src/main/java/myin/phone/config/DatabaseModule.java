package myin.phone.config;

import android.app.Application;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import myin.phone.data.app.HomeAppRepository;

import javax.inject.Singleton;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public MainDatabase mainDatabase(Application application) {
        return Room.databaseBuilder(application, MainDatabase.class, "main_database")
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public HomeAppRepository homeAppRepository(MainDatabase database) {
        return database.homeAppRepository();
    }

}
