This project provides a simple way to read and explore the data files
in the Bus Open Data Service archive.

https://data.datalibrary.uk/transport/BODS-ARCHIVE/

And then be able to do something interesting with the data. The initial
implementation allows reading files and printing out the data they
contain, optionally with some filtering.

This isn't (yet) a generic gtfs implementation; it makes a number of
assumptions about the input that happen to be true for what's in the
BODS archive.

See TIMETABLE.md and REALTIME.md for descriptions of the two different
areas of code.
