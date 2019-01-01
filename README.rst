JQuantLib is a free, open-source, comprehensive framework for
quantitative finance, written in 100% Java. It provides "quants" and
Java application developers several mathematical and statistical tools
needed for the valuation of shares, options, futures, swaps, and other
financial instruments.

JQuantLib is based on QuantLib, which is written in C++, aiming to be a
complete rewrite of QuantLib, offering features Java developers expect
to find. JQuantLib aims to be fast, correct, strongly typed,
well-documented, and user-friendly.

I'm changing JQuantlib to work through services rather than instance
methods. I'm not sure how well this will actually work but I currently
find it interesting and will continue to do so until that changes.

Quick guide for the impatient
=============================

On a Unix-like console

::

    # branch from Github with git
    git clone https://github.com/darkell/jquantlib.git

    # run demo number 9 (EquityOptions)
    cd jquantlib
    mvn verify
