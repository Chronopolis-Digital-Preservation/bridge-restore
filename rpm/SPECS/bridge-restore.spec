%define __jar_repack {%nil}
%define _prefix %{_usr}/local/chronopolis/restore
%define jar bridge-restore.jar
%define yaml bridge-restore.yaml
%define script bridge-restore.sh
%define build_time %(date +"%Y%m%d")

Name: bridge-restore
Version: %{ver}
Release: %{build_time}.el6
Source: bridge-restore.jar
Source1: bridge-restore.sh
Source2: bridge-restore.yaml
Summary: Chronopolis Ingest Server
License: BSD-3
URL: https://gitlab.umiacs.umd.edu/chronopolis
Group: System Environment/Daemons
autoprov: yes
autoreq: yes
BuildArch: noarch
BuildRoot: ${_tmppath}/build-%{name}-%{version}

%description
The Bridge Restore is a one time process which searches for Duracloud
Vault Snapshots which have been requested to be returned to their owner.

%install

%__install -D -m0644 "%{SOURCE0}" "%{buildroot}%{_prefix}/%{jar}"
%__install -D -m0755 "%{SOURCE1}" "%{buildroot}%{_prefix}/%{script}"
%__install -D -m0644 "%{SOURCE2}" "%{buildroot}%{_prefix}/%{yaml}"

%files

%defattr(-,root,root)
%dir %{_prefix}
%{_prefix}/%{jar}
%config(noreplace) %{_prefix}/%{yaml}
%config(noreplace) %{_prefix}/%{script}

%changelog

* Tue Mar 5 2019 Mike Ritter <shake@umiacs.umd.edu> 1.0.0-20190305
- Init rpm spec
