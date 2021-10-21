
# CompareFiles

Searches for duplicate files an deletes duplicates in remove paths.

    Usage: cf [-hisVw] [-c[=<contentLengthToCompare>]] [-l=<location>] [-k=<keepPaths>...]... [-r=<removePaths>...]...

    -l, --location=<location>
                            specifies which part of the relative pathname should be compared. Possible values: None, Name, Dir, Path
    -s, --size              compare file sizes
    -c, --content[=<contentLengthToCompare>]
                            compare files based on byte content up to contentLengthToCompare bytes, accepts units B/KB/MB/GB/TB/PB, implies -s
    -w, --imageDimensions   compare images based on width and height, implies -s for non-images
    -i, --image             compare images based on pixels, implies -w for images and -sc for non-images
    -k, --keepPaths=<keepPaths>...
                            all following parameters are paths where originals are located, which should be kept
                            Default: []
    -r, --removePaths=<removePaths>...
                            all following parameters are paths where removable duplicates are located
                            Default: []
    -h, --help              Show this help message and exit.
    -V, --version           Print version information and exit.
