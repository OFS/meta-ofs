#  Open FPGA Stack (OFS) software layer for Yocto

This layer provides the [Linux DFL kernel](https://github.com/OFS/linux-dfl)
and the [OPAE SDK](https://github.com/OFS/opae-sdk) for the [Yocto
Project\*](https://www.yoctoproject.org/).

## Getting started with Yocto

If you are new to the Yocto Project\*, start with the [overview and concepts
manual](https://docs.yoctoproject.org/overview-manual/), with more detailed
information available in the [reference
manual](https://docs.yoctoproject.org/ref-manual/) and the [Linux kernel
development manual](https://docs.yoctoproject.org/kernel-dev/).
To prepare a machine for building Yocto images, see the [system
requirements](https://docs.yoctoproject.org/ref-manual/system-requirements.html),
in particular the [required packages for the build
host](https://docs.yoctoproject.org/ref-manual/system-requirements.html#required-packages-for-the-build-host).
The [variables
glossary](https://docs.yoctoproject.org/ref-manual/variables.html) explains the
common variables used in the build system.

## Building the IOTG Yocto-based ESE example

This example takes the [IOTG Yocto-based ESE
BSP](https://github.com/intel/iotg-yocto-ese-manifest) as a basis
and substitutes the [Linux DFL kernel](https://github.com/OFS/linux-dfl)
including the latest [DFL drivers for FPGA
devices](https://docs.kernel.org/fpga/dfl.html) along with the [OPAE
SDK](https://github.com/OFS/opae-sdk) user space.
The image targets `x86_64` SoC FPGA devices but should boot on most UEFI-based
machines.

The build needs more than 100 GiB of disk space. As a reference point, on a
system with two Intel(R) Xeon(R) E5-2699 v4 for a total of 44 CPU cores,
the initial, non-incremental build takes less than an hour of wall time.

Fetch meta-ofs with
[submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) containing
requisite Yocto layers:

```
git clone --recurse-submodules --shallow-submodules https://github.com/OFS/meta-ofs
```

Build packages and create an image:

```
cd meta-ofs/examples/iotg-yocto-ese
TEMPLATECONF=$PWD/conf source openembedded-core/oe-init-build-env build
bitbake mc:x86-2022-minimal:core-image-full-cmdline
```

The resulting GPT disk image is available in uncompressed (`.wic`) and
compressed form (`.wic.gz`) in
`build/tmp-x86-2022-minimal-glibc/deploy/images/intel-corei7-64/`.

The [image type](https://docs.yoctoproject.org/ref-manual/images.html)
`core-image-full-cmdline` includes the familiar GNU core utilities,
as opposed to `core-image-minimal` which uses BusyBox instead.

The example build configuration files under `build/conf/` are copied from
[`examples/iotg-yocto-ese/conf/`](https://github.com/OFS/meta-ofs/tree/main/examples/iotg-yocto-ese/conf).
To customise the image, start by modifying
[`build/conf/local.conf`](https://github.com/OFS/meta-ofs/tree/main/examples/iotg-yocto-ese/conf/local.conf.sample)
and
[`build/conf/bblayers.conf`](https://github.com/OFS/meta-ofs/tree/main/examples/iotg-yocto-ese/conf/bblayers.conf.sample).

## Writing a Yocto image

If you downloaded a [release image](https://github.com/OFS/meta-ofs/releases),
verify its checksum:

```
sha256sum -c core-image-full-cmdline-intel-corei7-64-20230415005443.rootfs.wic.gz.sha256
```

On the target system, write the image to the storage, e.g., NVMe device:

```
zcat core-image-full-cmdline-intel-corei7-64-20230415005443.rootfs.wic.gz | dd of=/dev/nvme0n1 bs=1M status=progress
```

Relocate the GPT backup header to the end of the storage device:

```
sgdisk -e /dev/nvme0n1
```

`reboot` and select the storage device in the UEFI boot device menu.

## Downloading test images

For each push of a branch or tag to the
[OFS/meta-ofs](https://github.com/OFS/meta-ofs) repository, a [test
image](https://github.com/OFS/meta-ofs/actions/workflows/bitbake.yml) is
built and attached as an artifact to the build job. While the test images
may be downloaded using the web browser when logged into GitHub, this may
be inconvenient if the web browser runs on your local laptop while the
target system resides in remote location.

The [GitHub command-line client](https://cli.github.com/) provides a
convenient way of browsing and downloading test images on a remote machine.
Follow the [GitHub CLI installation
instructions](https://github.com/cli/cli#installation) for your OS and
authenticate to GitHub:

```
gh auth login
```

(This stores an OATH token in `$HOME/.config/gh/hosts.yml`.)

Browse the available test images, sorted newest to oldest:

```
gh run download --repo OFS/meta-ofs
```

Follow the instructions to select one or multiple test images, then press
`Enter` to download into the current directory.

## License

All metadata files (including, but not limited to `.bb`, `.bbappend`,
`.bbclass`, `.inc` and `.conf` files) are [MIT
licensed](https://github.com/OFS/meta-ofs/blob/main/COPYING.MIT) unless
otherwise stated. Source code included in tree for individual recipes is under
the LICENSE stated in the associated recipe (`.bb` file) unless otherwise
stated.
