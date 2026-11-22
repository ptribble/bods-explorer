The code under uk/co/petertribble/gtfs/realtime is designed to read the
archived timetable data files that can be found at

https://data.datalibrary.uk/transport/BODS-ARCHIVE/gtfsrt/

The java bindings I'm using are

https://gtfs.org/documentation/realtime/language-bindings/java/

which can be downloaded from

https://central.sonatype.com/artifact/org.mobilitydata/gtfs-realtime-bindings

If you select a version and browse, you can not only grab the relevant
jar but also the source and javadoc which are needed to actually develop
a useful application.

The realtime script here is a wrapper that avoids having to remember the
class names. To specify the input, supply either -f with the input
file, -d with the directory where the files are, or -z with the name of
the initial zip file (in the latter two cases it will look for a file
called gtfsrt.bin in the directory or zip file).
