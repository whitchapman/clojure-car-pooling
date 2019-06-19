# clojure-car-pooling
extends an Icelandic car pooling API

## Installation

clone this repository

## Usage

start a repl:

    $ lein repl

change to the core namespace (if necessary):

    > (ns clojure-car-pooling.core)

start the web server:

    > (start)

either browse to localhost port 4040:

    http://localhost:4040/

or use curl to query a list of drivers or passengers:

    $ curl -vX GET --header 'Accept: application/json' 'http://localhost:4040/rides/samferda-drivers/'
    $ curl -vX GET --header 'Accept: application/json' 'http://localhost:4040/rides/samferda-passengers/'

once you have a driver or passenger id, query for an individual extended set of data:

    $ curl -vX GET --header 'Accept: application/json' 'http://localhost:4040/rides/samferda-drivers/1041814'
    $ curl -vX GET --header 'Accept: application/json' 'http://localhost:4040/rides/samferda-passengers/1003583'

## License

Copyright © 2019 Whit Chapman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
