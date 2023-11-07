SUMMARY = "Open Programmable Acceleration Engine"
HOMEPAGE = "https://github.com/OFS/opae-sdk"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=5351f05d1aa340cb91bb885c2fd82fc7"
SRC_URI = "git://github.com/OFS/opae-sdk;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

DEPENDS = "\
    cli11 \
    hwloc \
    json-c \
    libedit \
    numactl \
    python3 \
    python3-jsonschema-native \
    python3-pip-native \
    python3-pybind11 \
    python3-pyyaml-native \
    spdlog \
    tbb \
    udev \
"

# Set package version from latest git tag and abbreviated commit hash
inherit gitpkgv
PKGV = "${GITPKGVTAG}"

inherit cmake pkgconfig python3native

# Tell pip install to use build dependencies provided by bitbake
# in the recipe's sysroot, instead of trying to download and
# install the build dependencies. The latter fails since bitbake
# builds recipes in an isolated sandbox without network access.
#
# https://github.com/OFS/meta-ofs/issues/1
# https://pip.pypa.io/en/stable/cli/pip_install/#cmdoption-no-build-isolation
#
# PIP_NO_CACHE_DIR and PIP_NO_BUILD_ISOLATION behave opposite to how they read
# https://github.com/pypa/pip/issues/5735
export PIP_NO_BUILD_ISOLATION = "off"

# Work around linking error for Python extension modules
#
# ld: cannot find crti.o: No such file or directory
# ld: cannot find crtbeginS.o: No such file or directory
# ld: cannot find -lstdc++: No such file or directory
# ld: cannot find -lm: No such file or directory
# ld: cannot find -lgcc_s: No such file or directory
# ld: cannot find -lpthread: No such file or directory
# ld: cannot find -lc: No such file or directory
# ld: cannot find -lgcc_s: No such file or directory
# ld: cannot find crtendS.o: No such file or directory
# ld: cannot find crtn.o: No such file or directory
#
LDFLAGS += "--sysroot=${STAGING_DIR_TARGET}"

# Adjust interpreter path from build to target machine.
# https://github.com/OFS/meta-ofs/issues/1#issuecomment-1515611193
# https://cgit.openembedded.org/openembedded-core/tree/meta/classes-recipe/setuptools3_legacy.bbclass?id=8e9ec03c73e8c09e223d6f6cce297df363991350
do_install:append() {
    sed -i -e 's:${PYTHON}:${bindir}/python3:g' ${D}${bindir}/*
}

FILES:${PN}+= "${prefix}/*"

RDEPENDS:${PN} = "\
    bash \
    python3-core \
    python3-jsonschema \
    python3-mmap \
    python3-pyyaml\
"
