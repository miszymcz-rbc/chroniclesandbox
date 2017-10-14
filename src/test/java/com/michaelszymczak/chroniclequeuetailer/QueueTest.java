package com.michaelszymczak.chroniclequeuetailer;

import com.google.common.collect.ImmutableMap;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.TailerDirection;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created 14/10/17.
 */
public class QueueTest {

  @Test
  public void shouldReallyNotFindLastMessageIfDirectionSetAfterMovingToEnd() throws Exception {
    // given
    final Map<String,Integer> foundNumbers = new HashMap<>();
    try (final DocumentContext dc = queue.acquireAppender().writingDocument()) {
      dc.wire().write().int32(999);
    }

    ExcerptTailer tailerEnd1 = queue.createTailer();
    tailerEnd1.direction(TailerDirection.BACKWARD);
    tailerEnd1.toEnd();

    ExcerptTailer tailerEnd2 = queue.createTailer();
    tailerEnd2.toEnd();
    tailerEnd2.direction(TailerDirection.BACKWARD);

    ExcerptTailer tailerEnd3 = queue.createTailer();
    tailerEnd3.direction(TailerDirection.BACKWARD);
    tailerEnd3.toEnd();

    // when
    tailerEnd1.readDocument(w -> foundNumbers.put("tailerEnd1", w.read("number").int32()));
    tailerEnd2.readDocument(w -> foundNumbers.put("tailerEnd2", w.read("number").int32()));
    tailerEnd3.readDocument(w -> foundNumbers.put("tailerEnd3", w.read("number").int32()));

    assertEquals(ImmutableMap.of(
            "tailerEnd1", 999,
            // "tailerEnd2", 999, // Really ?
            "tailerEnd3", 999
    ), foundNumbers);
  }

  @Before
  public void setUp() throws Exception {
    this.queuePath = Files.createTempDirectory("testQueue");
    this.queue = SingleChronicleQueueBuilder.binary(this.queuePath).rollCycle(RollCycles.MINUTELY).build();
  }

  @After
  public void tearDown() throws Exception {
    this.queue.close();
    FileUtils.deleteDirectory(queuePath.toFile());
  }

  private Path queuePath;
  private ChronicleQueue queue;
}