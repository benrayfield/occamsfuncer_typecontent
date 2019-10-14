# occamsfuncer
minimalist programming language for massively multiplayer collaboration at gaming-low-lag - every object is a funcall pair or type:content leaf, and can be secureHashed as ipfs-compatible id. Planning opencl and music tools optimizations.

fn is the core object type.

x.L().f(x.R()).equals(x), for all fn x.

type:content leaf examples:

fn:plus

fn:cons

n:5

image/jpeg:...bytes of jpg file... (any content-type)

(the ` char isnt displaying well in git website)

`:H:abcH:def where abc and def are 34 byte ipfs ids (hash prefix such as sha256 then 32 bytes of hash). This is a funcall pair. ` is the name of this cuz that char is used in unlambda and iota languages.

All such objects are either a leaf or ` funcall pair.

The hash ids are lazyEvaled so dont slow things down except when observed the first time.

Treemaps are trie-like and have a max depth of the number of bits in an id and an average depth of log of their size.

AvlTreeList.

S and K lambda based controlflow in the simplest cases but will also have higher level looping forms, which are also lambdas. There is no mutable system state except Gas.top which is a mutable double for how much computing resources are allowed in calls deeper than each point, as Op.gas and Op.spend. Every object is a self contained lazyEval-hashed merkle forest.

Caches <func,param,return> so can fibonacci recursively in linear instead of exponential time.

(these things are incomplete, but the core design is nearly finished).

Older fork of occamsfuncer is at https://github.com/benrayfield/occamsfuncer_old, some of which will be used, but not the coretypes. There are no coretypes anymore in funcall pairs.
