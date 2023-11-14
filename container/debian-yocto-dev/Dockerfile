FROM debian:bookworm

# https://docs.yoctoproject.org/ref-manual/system-requirements.html#ubuntu-and-debian
RUN apt-get -y update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y dist-upgrade \
    build-essential \
    chrpath \
    cpio \
    debianutils \
    diffstat \
    file \
    gawk \
    gcc \
    git \
    iputils-ping \
    libegl1-mesa \
    liblz4-tool \
    libsdl1.2-dev \
    locales-all \
    mesa-common-dev \
    pylint \
    python3 \
    python3-git \
    python3-jinja2 \
    python3-pexpect \
    python3-pip \
    python3-subunit \
    socat \
    texinfo \
    unzip \
    wget \
    xterm \
    xz-utils \
    zstd \
    && apt-get -y clean

# BitBake requires running as a non-root user, which is advisable in
# any case to replicate the common case of building as non-root.
ARG BITBAKE_UID
RUN useradd ${BITBAKE_UID:+--uid "$BITBAKE_UID"} --create-home --shell /bin/bash bitbake
RUN install -d -o bitbake -g bitbake /bitbake
VOLUME /bitbake
USER bitbake
