CIME2CSV
========

A simple utility for splitting CIME file into separate CSV files.

What is a CIME file?
==========

A CIME file is a file format used in State Grid information systems to store devices' and measurement points' information and their relationship.


Why creating this utility?
==========

We have lots of CSV parsing tools like apache commons-csv but we do not have a free and good CIME file parser.
As CIME file is easy to be converted to CSV files, we can just do so and make use of exisitng CSV parsers.

