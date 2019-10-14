# occamsfuncer
minimalist programming language for massively multiplayer collaboration at gaming-low-lag - every object is a funcall pair or type:content leaf, and can be secureHashed as ipfs-compatible id. Planning opencl and music tools optimizations.

fn is the core object type.

x.L().f(x.R()).equals(x), for all fn x. Each leaf type has a certain number of curries, before which (fn:plus 3) returns (fn:plus 3) but (fn:plus 3 4) returns 7. There will be more convenient syntaxes, especially for shared branches being #localNamed and for abstraction of s-lambda levels. All fn:xyz (for any xyz) are defined as part of the occamsfuncerVM and are not runtime variable names. There are no variable names because there are no variables, only constants. Everything is a kind of number, though its syntax will be intuitive enough for Humans age 5 and up (you cant break it, infinite loops are impossible for example). This system is similar to urbit but more designed to be intuitive for Humans and optimizable.

Id types start with H: (normal hash id) or h: (weakref is a func that returns 1 if its param is the thing its a weakref to in H: form, else 0) or ?: followed by an arbitrary local id not to be shared across untrusted borders. Only H: and h: are shared across untrusted borders as they are the merkle-forest (blockchain-like) kinds of ids suffixed by ipfs-compatible id.

Any system, such as ipfs, bittorrent, ethereum, local, authoritarian websites, andOr direct peer to peer, can all work together as 1 network, though I'm skeptical of ethereum being 32 byte words and these being 36 byte which would further slow it down.

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

It will come with a fn:ed25519 function for fast digital signatures. Other algorithms can be either added at the VM level or derived at user level (slower) later.

S and K lambda based controlflow in the simplest cases but will also have higher level looping forms, which are also lambdas. There is no mutable system state except Gas.top which is a mutable double for how much computing resources are allowed in calls deeper than each point, as Op.gas and Op.spend. Every object is a self contained lazyEval-hashed merkle forest.

Caches <func,param,return> so can fibonacci recursively in linear instead of exponential time.

(these things are incomplete, but the core design is nearly finished).

Older fork of occamsfuncer is at https://github.com/benrayfield/occamsfuncer_old, some of which will be used, but not the coretypes. There are no coretypes anymore in funcall pairs.
