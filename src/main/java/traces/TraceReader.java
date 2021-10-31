/*
 * Copyright 2015 Ben Manes. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package traces;

import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A reader to an access trace.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
public interface TraceReader {

    /** The event features that this trace supports. */
    //Set<Policy.Characteristic> characteristics();

    /**
     * Creates a stream that lazily reads the trace source.
     * <p>
     * If timely disposal of underlying resources is required, the try-with-resources construct should
     * be used to ensure that the stream's {@link Stream#close close} method is invoked after the
     * stream operations are completed.
     *
     * @return a lazy stream of cache events
     */
    Stream<AccessEvent> events() throws IOException;
    default Stream<AccessEvent>[] eventsByFile() throws IOException{
        return null;
    }

    /** A trace reader that that does not contain external event metadata. */
    interface KeyOnlyTraceReader extends TraceReader {

//        @Override default Set<Policy.Characteristic> characteristics() {
//            return ImmutableSet.of();
//        }

        @Override default Stream<AccessEvent> events() throws IOException {
            return keys().mapToObj(AccessEvent::forKey);
        }
        default Stream<AccessEvent>[] eventsByFile() throws IOException{
            return new Stream[]{events()};
        }
        LongStream keys() throws IOException;
    }
}
