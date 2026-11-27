The code under uk/co/petertribble/gtfs/timetable is designed to read the
archived timetable data files that can be found at

https://data.datalibrary.uk/transport/BODS-ARCHIVE/timetables/

There's a class GtfsFoo for each thing, and then a ReadFoo class that reads
the data files. Each Read class has a main method so it can be used as a
standalone application to read the data files and format them, optionally
allowing some filtering or selection.

The timetable script here is a wrapper that avoids having to remember the
class names. To specify the input, supply -d with the directory where the
files are. The wrapper also accepts -D to specify the date  (in YYYYMMDD
format) and -R the region, and will automatically download and cache the
appropriate data files for you.

The specification is here:

https://gtfs.org/documentation/schedule/reference/

Examples:

The -R is required, it specifies the region (one of east_anglia,
east_midlands, london, north_east, north_west, scotland, south_east,
south_west, wales, west_midlands, yorkshire)

The -D specifies the date in YYYYMMDD format; if not given it will
assume today's date.

To simply print the counts of items

./timetable -R east_anglia -D 20261123 all

To simply print the counts of items, with counts by operator

./timetable -R east_anglia -D 20261123 all -v

To show departures by hour

./timetable -R east_anglia -D 20261123 all -h

To show departures by hour for Staegcoach East (OP118)

./timetable -R east_anglia -D 20261123 all -ho OP118

To print out the list of agencies

./timetable -R east_anglia -D 20261123 agency

Instead of agency you can use route, stop, stop_time, trip and it will
simply print that out. Each accepts additional arguments to filter the
list.

To produce a heat map of stops in a given area

./timetable -R east_anglia -D 20261123 mapstops -y 52.1 -Y 52.3 -x -0.05 -X 0.35 -G

The -y/-Y/-x/-X give the bounding latitude and longitude. The example
above shows Cambridge.

To produce a heat map of stop times (essentially, service frequency) in
a given area

./timetable -R east_anglia -D 20261123 mapstoptimes -y 52.1 -Y 52.3 -x -0.05 -X 0.35 -G
