package test;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public class EventListenerGenericTest {
    private boolean triggered = false;

    public static class SomeEvent extends Event {}

    public class SomeListener implements Consumer<SomeEvent> {

        @Override
        public void accept(SomeEvent event) {
            EventListenerGenericTest.this.triggered = true;
        }
    }

    @Test
    public void test() {
        var bus = new BusBuilder().build();

        bus.addListener(new SomeListener());
        bus.post(new SomeEvent());

        Assertions.assertTrue(triggered);
    }
}
