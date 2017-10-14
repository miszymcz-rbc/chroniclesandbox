package com.michaelszymczak.foo;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.TailerDirection;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);


    public String getGreeting() {
        return "Hello Foo.";
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Setup setup = new Setup("foo", true);
        try (ServiceControl serviceControl = setup.serviceControl();
             Job job = new Job(serviceControl)) {
//            job.start();
            activity(setup.serviceInput());
//            Thread.sleep(Duration.ofSeconds(1).toMillis());
//            job.stop();
            result(setup.serviceInput());
        }
        LOG.info("END");
    }

    private static void activity(Path serviceInputQueuePath) throws InterruptedException {
        try (ChronicleQueue inputQueue = SingleChronicleQueueBuilder.binary(serviceInputQueuePath).rollCycle(RollCycles.MINUTELY).build()) {
            try (final DocumentContext dc = inputQueue.acquireAppender().writingDocument()) {
                dc.wire().write().int32(999);
            }
        }
    }

    private static void result(Path serviceInputQueuePath) {
        try (ChronicleQueue inputQueue = new QueueFactory(serviceInputQueuePath).create()) {
            System.out.println("====== WORKS:");
            ExcerptTailer tailerEnd = inputQueue.createTailer();
            tailerEnd.direction(TailerDirection.BACKWARD);
            tailerEnd.toEnd();
            tailerEnd.readDocument(w -> System.out.println("End number: " + w.read("number").int32()));

            System.out.println("====== DOES NOT WORK:");


            ExcerptTailer tailerEnd2 = inputQueue.createTailer();
            tailerEnd2.toEnd();
            tailerEnd2.direction(TailerDirection.BACKWARD);
            tailerEnd2.readDocument(w -> System.out.println("End number: " + w.read("number").int32()));

            System.out.println("======");
        }
    }

    private static void print(long idx, ExcerptTailer tailer) {
        System.out.println("#");
        tailer.moveToIndex(idx);
        tailer.direction(TailerDirection.BACKWARD);
        tailer.readDocument(w -> System.out.println(idx + ": " + w.read("number").int32()));
        tailer.readDocument(w -> System.out.println(idx + ": " + w.read("number").int32()));
    }
}
