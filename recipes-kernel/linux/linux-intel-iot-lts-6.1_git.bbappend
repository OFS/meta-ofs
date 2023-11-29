KBRANCH = "fpga-ofs-dev-6.1-lts"
KERNEL_SRC_URI = "git://github.com/OFS/linux-dfl;protocol=https;branch=${KBRANCH};name=machine"
SRC_URI = "${KERNEL_SRC_URI}"
SRCREV_meta = "${AUTOREV}"
SRCREV_machine = "f34a2d519aa955a7f36dbf418f6c3e9cc11eca4a"
LINUX_VERSION = "6.1"
LINUX_VERSION_EXTENSION = "-dfl-${@bb.fetch2.get_srcrev(d).split('_')[1]}"

# Allow mismatch between kernel package and source versions,
# e.g., 5.15+gitAUTOINC+441f5fe000_66b0076c2c versus 5.15.92.
# The kernel version is not known a priori since the recipe
# builds the latest commit of the given linux-dfl branch.
KERNEL_VERSION_SANITY_SKIP = "1"

# Configure Linux FPGA Device Feature List (DFL) driver
# https://github.com/OFS/linux-dfl/wiki#kernel-configuration
FILESEXTRAPATHS:append := "${THISDIR}:"
SRC_URI += "file://dfl-config.scc"
