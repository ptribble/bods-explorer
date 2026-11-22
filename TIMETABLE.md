The code under uk/co/petertribble/gtfs/timetable is designed to read the
archived timetable data files that can be found at

https://data.datalibrary.uk/transport/BODS-ARCHIVE/timetables/

There's a class Foo for each thing, and then a ReadFoo class that reads
the data files. Each Read class has a main method so it can be used as a
standalone application to read the data files and format them, optionally
allowing some filtering or selection.

The timetable script here is a wrapper that avoids having to remember the
class names. To specify the input, supply either -f with the input file,
or -d with the directory where the files are. The wrapper also accepts
-D to specify the date  (in YYYYMMDD format) and -R the region, and will
automatically download and cache the appropriate data files for you.

The specification is here:

https://gtfs.org/documentation/schedule/reference/
