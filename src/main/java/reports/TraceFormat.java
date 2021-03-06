///*
// * Copyright 2015 Ben Manes. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package parser;
//
//import com.google.common.base.Splitter;
//import com.google.common.collect.Iterables;
//import com.google.common.collect.Sets;
//import parser.adapt_size.AdaptSizeReader;
//import parser.address.AddressTraceReader;
//import parser.address.penalties.AddressPenaltiesTraceReader;
//import parser.arc.ArcTraceReader;
//import parser.cache2k.Cache2kTraceReader;
//import parser.climb.ClimbTraceReader;
//import parser.corda.CordaTraceReader;
//import parser.gradle.GradleTraceReader;
//import parser.lirs.LirsTraceReader;
//import parser.scarab.ScarabTraceReader;
//import parser.snia.cambridge.CambridgeTraceReader;
//import parser.synthetic.SyntheticTrace;
//import parser.umass.network.YoutubeTraceReader;
//import parser.umass.storage.StorageTraceReader;
//import parser.wikipedia.WikipediaTraceReader;
//import policies.Policy;
//import traces.AccessEvent;
//import traces.TraceReader;
//import utils.AccessEvent;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Set;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
//import static java.util.Locale.US;
//import static java.util.stream.Collectors.toList;
//
///**
// * The trace file formats.
// *
// * @author ben.manes@gmail.com (Ben Manes)
// */
//@SuppressWarnings("ImmutableEnumChecker")
//public enum TraceFormat {
//    ADDRESS(AddressTraceReader::new),
//    ADDRESS_PENALTIES(AddressPenaltiesTraceReader::new),
//    ADAPT_SIZE(AdaptSizeReader::new),
//    ARC(ArcTraceReader::new),
//    CACHE2K(Cache2kTraceReader::new),
//    CLIMB(ClimbTraceReader::new),
//    CORDA(CordaTraceReader::new),
//    GRADLE(GradleTraceReader::new),
//    LIRS(LirsTraceReader::new),
//    SCARAB(ScarabTraceReader::new),
//    SNIA_CAMBRIDGE(CambridgeTraceReader::new),
//    UMASS_STORAGE(StorageTraceReader::new),
//    UMASS_YOUTUBE(YoutubeTraceReader::new),
//    WIKIPEDIA(WikipediaTraceReader::new),
//    SYNTHETIC(SyntheticTrace::new);
//
//    private final Function<String, TraceReader> factory;
//
//    TraceFormat(Function<String, TraceReader> factory) {
//        this.factory = factory;
//    }
//
//    /**
//     * Returns a new reader for streaming the events from the trace file.
//     *
//     * @param filePaths the path to the files in the trace's format
//     * @return a reader for streaming the events from the file
//     */
//    public TraceReader readFiles(List<String> filePaths) {
//        return new TraceReader() {
//
////            @Override public Set<Policy.Characteristic> characteristics() {
////                return readers().stream()
////                        .flatMap(reader -> reader.characteristics().stream())
////                        .collect(Sets.toImmutableEnumSet());
////            }
//
//            @Override public Stream<AccessEvent> events() throws IOException {
//                Stream<AccessEvent> events = Stream.empty();
//                for (TraceReader reader : readers()) {
//                    events = Stream.concat(events, reader.events());
//                }
//                return events;
//            }
//
//            @Override
//            public Stream<AccessEvent>[] eventsByFile() throws IOException {
//                Stream<AccessEvent> events = Stream.empty();
//                List<TraceReader> readers = readers();
//                Object[] x= readers.stream().map(reader-> {
//                    try {
//                        return reader.events();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return Stream.empty();
//                }).toArray(value -> new Stream[readers.size()]);
//                try{
//                    return (Stream<AccessEvent>[]) x;
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                return null;
////        for (TraceReader reader : readers()) {
////          events = Stream.concat(events, reader.events());
////        }
////        return events;
//            }
//
//            private List<TraceReader> readers() {
//                return filePaths.stream().map(path -> {
//                    List<String> parts = Splitter.on(':').limit(2).splitToList(path);
//                    TraceFormat format = (parts.size() == 1) ? TraceFormat.this : named(parts.get(0));
//                    return format.factory.apply(Iterables.getLast(parts));
//                }).collect(toList());
//            }
//        };
//    }
//
//    /** Returns the format based on its configuration name. */
//    public static TraceFormat named(String name) {
//        return TraceFormat.valueOf(name.replace('-', '_').toUpperCase(US));
//    }
//}
