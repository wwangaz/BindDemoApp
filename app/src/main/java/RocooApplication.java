import android.app.Application;
import android.content.Context;

import com.dodola.rocoofix.RocooFix;

/**
 * Created by wangweimin on 16/7/18.
 */
public class RocooApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RocooFix.init(this);
    }
}
