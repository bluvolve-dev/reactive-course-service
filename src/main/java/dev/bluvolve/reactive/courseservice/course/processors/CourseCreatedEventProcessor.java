package dev.bluvolve.reactive.courseservice.course.processors;

import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Processes the 'CourseCreated' event.
 */
@Slf4j
@Component
public class CourseCreatedEventProcessor
        implements ApplicationListener<CourseCreated>,
        Consumer<FluxSink<CourseCreated>> {

    private final Executor executor;
    private final BlockingQueue<CourseCreated> queue = new LinkedBlockingQueue<>();

    CourseCreatedEventProcessor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(CourseCreated event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<CourseCreated> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    CourseCreated event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
